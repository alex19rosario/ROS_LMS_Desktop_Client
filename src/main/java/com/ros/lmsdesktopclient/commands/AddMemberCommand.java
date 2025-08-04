package com.ros.lmsdesktopclient.commands;

import com.ros.lmsdesktopclient.dtos.AddMemberDTO;
import com.ros.lmsdesktopclient.models.MemberModel;
import com.ros.lmsdesktopclient.services.service.MemberService;
import com.ros.lmsdesktopclient.util.enums.AlertContents;
import com.ros.lmsdesktopclient.util.enums.Alerts;
import com.ros.lmsdesktopclient.util.TokenHandler;
import com.ros.lmsdesktopclient.util.enums.Views;
import com.ros.lmsdesktopclient.util.exceptions.*;
import com.ros.lmsdesktopclient.util.validators.EmailValidator;
import com.ros.lmsdesktopclient.util.validators.PhoneNumberValidator;
import javafx.concurrent.Task;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class AddMemberCommand extends Command{

    private final MemberModel member;
    private final MemberService memberService;
    private final Command openLoginViewCommand;

    @Inject
    public AddMemberCommand(MemberModel member, MemberService memberService){
        this.member = member;
        this.memberService = memberService;
        this.openLoginViewCommand = new OpenViewCommand(Views.LOGIN);
        setOnCommandSuccess(this::onSuccess);
        setOnCommandFailure(this::onFailure);
    }

    @Override
    protected Task<Void> createCommandTask() {

        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                checkForm(member);
                AddMemberDTO memberDTO = mapper.apply(member);
                memberService.addMember(memberDTO);
                return null;
            }
        };
    }

    private void onSuccess(){
        setAlert(Alerts.MEMBER_ADDED_SUCCESS);
        getAlert().getModal(AlertContents.MEMBER_ADDED_OK.getValue());
        //reset screen
        member.clear();
    }

    private void onFailure(){
        Throwable exception = getCommandTask().getException();

        Alerts alert = switch (exception){
            case EmptyFieldsException ignored -> Alerts.EMPTY_FIELDS_WARN;
            case InvalidGovernmentIDException ignored -> Alerts.INVALID_ID_ERROR;
            case InvalidPhoneNumberException ignored -> Alerts.INVALID_PHONE_ERROR;
            case InvalidEmailException ignored -> Alerts.INVALID_EMAIL_ERROR;
            case InvalidPasswordException ignored -> Alerts.INVALID_PASSWORD_ERROR;
            case PasswordsDoNotMatchException ignored -> Alerts.UNMATCHED_PASSWORDS_ERROR;
            case NetworkException ignored -> Alerts.NETWORK_ERROR;
            case ServerErrorException ignored -> Alerts.SERVER_ERROR;
            case ExpiredSessionException ignored -> Alerts.EXPIRED_SESSION_ERROR;
            case MemberAlreadyExistException ignored -> Alerts.EXISTING_MEMBER_ERROR;
            case UsernameAlreadyExistException ignored -> Alerts.EXISTING_USERNAME_ERROR;
            case EmailAlreadyExistException ignored -> Alerts.EXISTING_EMAIL_ERROR;
            case InvalidDateOfBirthException ignored -> Alerts.INVALID_DATE_OF_BIRTH;
            default -> throw new IllegalStateException("Unexpected exception: " + exception);
        };
        String content = exception.getMessage();
        setAlert(alert);
        getAlert().getModal(content);

        if(exception instanceof ExpiredSessionException){
            openLoginViewCommand.execute();
        }
    }

    private void checkForm(MemberModel model) throws Exception {

        Map<Predicate<MemberModel>, Exception> validations = new LinkedHashMap<>();
        validations.put(hasNoEmptyFields, new EmptyFieldsException("Add Member Form: there are empty fields"));
        validations.put(hasValidGovernmentID, new InvalidGovernmentIDException("Add Member Form: Invalid Government ID"));
        validations.put(hasValidPhoneNumber, new InvalidPhoneNumberException("Add Member Form: Invalid Phone Number"));
        validations.put(hasValidEmail, new InvalidEmailException("Add Member Form: Invalid Email"));
        validations.put(hasValidPassword, new InvalidPasswordException("Add Member Form: Invalid Password"));
        validations.put(passwordsDoMatch, new PasswordsDoNotMatchException("Add Member Form: Passwords do not match"));
        validations.put(hasValidDateOfBirth, new InvalidDateOfBirthException("Add Member Form: Date of Birth cannot be in the future"));

        for (Map.Entry<Predicate<MemberModel>, Exception> entry : validations.entrySet()) {
            if (!entry.getKey().test(model)) {
                throw entry.getValue();
            }
        }
    }

    private final Predicate<MemberModel> hasNoEmptyFields = MemberModel::isComplete;

    private final Predicate<MemberModel> hasValidGovernmentID = member -> member.getGovernmentID().length() > 5;

    private final Predicate<MemberModel> hasValidEmail = member -> EmailValidator.getInstance().hasValidEmail(member.getEmail());

    private final Predicate<MemberModel> hasValidPhoneNumber = member -> PhoneNumberValidator.getInstance().hasValidPhoneNumber(member.getPhone());

    private final Predicate<MemberModel> hasValidPassword = member -> member.getPassword().length() > 4;

    private final Predicate<MemberModel> passwordsDoMatch = member -> member.getPassword().equalsIgnoreCase(member.getRepeatedPassword());

    private final Predicate<MemberModel> hasValidDateOfBirth = member -> member.getDateOfBirth().isBefore(LocalDate.now());

    private final Function<MemberModel, AddMemberDTO> mapper = memberModel -> new AddMemberDTO(
            memberModel.getGovernmentID(),
            memberModel.getFirstName(),
            memberModel.getLastName(),
            memberModel.getPhone(),
            memberModel.getDateOfBirth(),
            memberModel.getSex(),
            memberModel.getEmail(),
            memberModel.getUsername(),
            memberModel.getPassword(),
            TokenHandler.getInstance().getUsername());
}

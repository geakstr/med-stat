$(document).ready(function () {
    validateAdminUserRegistrationForm();
});

var $adminUserRegistrationForm = $('#admin-user-registration-form');
var $adminUserRegistrationFormFirstName = $('#admin-user-registration-form-first-name');
var $adminUserRegistrationFormLastName = $('#admin-user-registration-form-last-name');
var $adminUserRegistrationFormBirthDate = $('#admin-user-registration-form-birth-date');
var $adminUserRegistrationFormEmail = $('#admin-user-registration-form-email');
var $adminUserRegistrationFormPassword = $('#admin-user-registration-form-password');
var $adminUserRegistrationFormTelephone = $('#admin-user-registration-form-telephone');
var $adminUserRegistrationFormRole1 = $('#admin-user-registration-form-role-1');
var $adminUserRegistrationFormRole2 = $('#admin-user-registration-form-role-2');
var $adminUserRegistrationFormSubmit = $('#admin-user-registration-form-submit');
var $adminUserRegistrationFormPasswordHelper = $('#admin-user-registration-form-password-helper');
var $adminUserRegistrationFormMessage = $('#admin-user-registration-form-message');

$adminUserRegistrationForm.submit(function (event) {
    event.preventDefault();
    submitForm($(this), function (response) {
        if (response.type === "error") {
            $adminUserRegistrationFormMessage.show();
            $adminUserRegistrationFormMessage.find('p').text(response.message);
        } else {
            $adminUserRegistrationFormMessage.hide();
            $adminUserRegistrationForm.trigger('reset');
            $.get("/admin/generate-password/6", function (password) {
                $adminUserRegistrationFormPassword.val(password);
            });
        }
    });
    return false;
});

$adminUserRegistrationForm.find('input').on('input change', function (event) {
    validateAdminUserRegistrationForm();
});

function validateAdminUserRegistrationForm() {
    var firstName = $adminUserRegistrationFormFirstName.val().trim();
    var lastName = $adminUserRegistrationFormLastName.val().trim();
    var birthDate = $adminUserRegistrationFormBirthDate.val().trim();
    var email = $adminUserRegistrationFormEmail.val().trim();
    var password = $adminUserRegistrationFormPassword.val().trim();

    var disabled = true;
    if (firstName.length > 0 && lastName.length > 0 && password.length >= 6 && emailValidate(email)) {
        disabled = false;
    } else {
        disabled = true
    }

    try {
        new Date(birthDate).toISOString()
    } catch (e) {
        disabled = true;
    }

    $adminUserRegistrationFormSubmit.attr('disabled', disabled);

    if (passwordReliabilityTest(password)) {
        $adminUserRegistrationFormPasswordHelper.hide();
    } else {
        $adminUserRegistrationFormPasswordHelper.show();
    }
}

function emailValidate(email) {
    var regex = /^[а-яёА-ЯЁa-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[а-яёА-ЯЁa-zA-Z0-9-]+(?:\.[а-яёА-ЯЁa-zA-Z0-9-]+)*$/;
    return regex.test(email);
}

function passwordReliabilityTest(password) {
    var digitCnt = 0;
    var charCnt = 0;

    for (var i = 0; i < password.length; i++) {
        if (isNumber(password[i])) {
            digitCnt++;
        } else {
            charCnt++;
        }
    }
    return password.length >= 6 && digitCnt >= 2 && charCnt >= 2;
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function submitForm($form, callback) {
    var action = $form.attr('action');
    var json = formToJson($form);

    $.ajax({
        type: 'POST',
        url: action,
        dataType: "json",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(json),
        success: callback
    });
}

function formToJson($form) {
    var json = {};

    var $inputs = $form.find('input');
    for (var i = 0; i < $inputs.length; i++) {
        var $input = $($inputs[i]);
        if ($input.attr('type') === 'submit') {
            continue;
        }
        if ($input.attr('type') === 'radio' || $input.attr('type') === 'checkbox') {
            if ($input.attr('checked')) {
                json[$input.attr('name')] = $input.val();
            }
        } else {
            json[$input.attr('name')] = $input.val();
        }
    }
    return json;
}

$('div[data-toggle=buttons] label').on('click', function () {
    var $this = $(this);
    var $curRadio = $this.find('input[type=radio]');
    if ($curRadio.length === 1) {
        var $radios = $this.parents('form').find('input[name=' + $curRadio.attr('name') + ']');

        for (var i = 0; i < $radios.length; i++) {
            var $radio = $($radios[i]);
            if ($radio[0] === $curRadio[0]) {
                $radio.attr('checked', true);
            } else {
                $radio.attr('checked', false);
            }
        }
    }
});

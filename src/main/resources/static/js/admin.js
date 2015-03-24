$(document).ready(function() {
  validateAdminUserRegistrationForm();
});

var $adminUserRegistrationForm = $('#admin-user-registration-form');
var $adminUserRegistrationFormFirstName = $('#admin-user-registration-form-first-name');
var $adminUserRegistrationFormLastName = $('#admin-user-registration-form-last-name');
var $adminUserRegistrationFormBirthDateDay = $('#admin-user-registration-form-birth-date-day');
var $adminUserRegistrationFormBirthDateMonth = $('#admin-user-registration-form-birth-date-month');
var $adminUserRegistrationFormBirthDateYear = $('#admin-user-registration-form-birth-date-year');
var $adminUserRegistrationFormEmail = $('#admin-user-registration-form-email');
var $adminUserRegistrationFormTelephone = $('#admin-user-registration-form-telephone');
var $adminUserRegistrationFormRole1 = $('#admin-user-registration-form-role-1');
var $adminUserRegistrationFormRole2 = $('#admin-user-registration-form-role-2');
var $adminUserRegistrationFormSubmit = $('#admin-user-registration-form-submit');
var $adminUserRegistrationFormMessage = $('#admin-user-registration-form-message');

$($adminUserRegistrationFormBirthDateDay).on('keypress', function(event) {
  event = (event) ? event : window.event;

  var charCode = (event.which) ? event.which : event.keyCode;
  if (!isDigit(charCode)) {
    event.preventDefault();
    return false;
  }

  return true;
});

$($adminUserRegistrationFormBirthDateMonth).on('keypress', function(event) {
  event = (event) ? event : window.event;

  var charCode = (event.which) ? event.which : event.keyCode;
  if (!isDigit(charCode)) {
    event.preventDefault();
    return false;
  }

  return true;
});

$($adminUserRegistrationFormBirthDateYear).on('keypress', function(event) {
  event = (event) ? event : window.event;

  var charCode = (event.which) ? event.which : event.keyCode;
  if (!isDigit(charCode)) {
    event.preventDefault();
    return false;
  }

  return true;
});


$adminUserRegistrationForm.submit(function(event) {
  event.preventDefault();

  $adminUserRegistrationFormSubmit.button('loading');

  submitForm($(this), function(response) {
    if (response.type === "error") {
      $adminUserRegistrationFormMessage.show();
      $adminUserRegistrationFormMessage.find('p').text(response.message);
    } else {
      $adminUserRegistrationFormMessage.hide();

      $adminUserRegistrationForm.trigger('reset');
    }

    $adminUserRegistrationFormSubmit.button('reset');
    $adminUserRegistrationFormSubmit.attr('disabled', true);
  }, function(request, status, error) {
    $adminUserRegistrationFormMessage.show();
    $adminUserRegistrationFormMessage.find('p').text('Что-то пошло не так! Пожалуйста, попробуйте еще через некоторое время');

    $adminUserRegistrationFormSubmit.button('reset');
  });

  return false;
});

$adminUserRegistrationForm.find('input').on('input change', function(event) {
  validateAdminUserRegistrationForm();

  $adminUserRegistrationFormMessage.find('p').text('');
  $adminUserRegistrationFormMessage.hide();
});

function validateAdminUserRegistrationForm() {
  var firstName = $adminUserRegistrationFormFirstName.val().trim();
  var lastName = $adminUserRegistrationFormLastName.val().trim();
  var birthDateDay = $adminUserRegistrationFormBirthDateDay.val().trim();
  var birthDateMonth = $adminUserRegistrationFormBirthDateMonth.val().trim();
  var birthDateYear = $adminUserRegistrationFormBirthDateYear.val().trim();
  var birthDate = birthDateDay + "/" + birthDateMonth + "/" + birthDateYear;
  var email = $adminUserRegistrationFormEmail.val().trim();

  var disabled = true;
  if (firstName.length > 0 && lastName.length > 0 && emailValidate(email) &&
    birthDateDay.length > 0 && birthDateMonth.length > 0 && birthDateYear > 0) {
    disabled = false;
  } else {
    disabled = true
  }

  if (!disabled) {
    disabled = !isActualDate(birthDateDay, birthDateMonth, birthDateYear);
  }

  $adminUserRegistrationFormSubmit.attr('disabled', disabled);
}

function isActualDate(day, month, year) {
  var tempDate = new Date(year, --month, day);
  return month === tempDate.getMonth();
};

function emailValidate(email) {
  var regex = /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
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

function isDigit(charCode) {
  if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    return false;
  }
  return true;
}

function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function submitForm($form, success, error) {
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
    success: success,
    error: error
  });
}

function formToJson($form) {
  var json = {};
  var formData = $form.serializeArray();
  $.each(formData, function() {
    if (json[this.name]) {
      if (!json[this.name].push) {
        json[this.name] = [json[this.name]];
      }
      json[this.name].push(this.value || '');
    } else {
      json[this.name] = this.value || '';
    }
  });

  return json;
}
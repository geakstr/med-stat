function isActualDate(day, month, year) {
  var tempDate = new Date(year, --month, day);
  return month === tempDate.getMonth();
};

function isValidEmail(email) {
  var regex = /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  return regex.test(email);
}

function isReliablePassword(password) {
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

function submitForm($form, timeout, success, error) {
  var action = $form.attr('action');
  var json = formToJson($form);
  $.ajax({
    type: 'POST',
    url: action,
    dataType: "json",
    timeout: timeout,
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    data: JSON.stringify(json),
  }).done(function(data) {
    success(data);
  }).fail(function(jqXHR, textStatus, err) {
    if (textStatus == 'timeout') {
      alert('Истекло время ожидания. Попробуйте еще раз');
    }
    alert(err);
    error(jqXHR, textStatus, err);
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

$.postJSON = function(url, data, callback) {
    return jQuery.ajax({
    headers: { 
        'Accept': 'application/json',
        'Content-Type': 'application/json' 
    },
    'type': 'POST',
    'url': url,
    'data': JSON.stringify(data),
    'dataType': 'json',
    'success': callback
    });
};
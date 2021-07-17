// Logout function
$(document).ready(function () {
  $("#logoutLink").on("click", function (e) {
    e.preventDefault();
    document.logoutForm.submit();
  });
});

// Delete user function
$(document).ready(function () {
  $(".link-delete").click(function (e) {
    e.preventDefault();
    link = $(this);
    userId = link.attr("userId");
    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text(
      "Are you sure you want to delete this user with id [" + userId + "] ?"
    );
    $("#confirmModal").modal();
  });
});

// Clear user filter
function clearFilter() {
  window.location = "[[@{/users}]]";
}

// Click create / edit cancel button
$(document).ready(function () {
  $("#buttonCancel").on("click", function () {
    window.location = "[[@{/users}]]";
  });

  //Check file size before showing thumbnail
  $("#fileImage").change(function () {
    fileSize = this.files[0].size;
    if (fileSize > 1024576) {
      this.setCustomValidity("You must choose an image less than 1 MB");
      this.reportValidity();
    } else {
      this.setCustomValidity("");
      showImageThumbnail(this);
    }
  });
});

// Code to show thumbnail of photo
function showImageThumbnail(fileInput) {
  var file = fileInput.files[0];
  var reader = new FileReader();
  reader.onload = function (e) {
    $("#thumbnail").attr("src", e.target.result);
  };
  reader.readAsDataURL(file);
}

// Check unique email function using ajax call
function checkEmailUnique(form) {
  url = "[[@{/users/check-email}]]";
  userEmail = $("#email").val();
  csrfValue = $("input[name='_csrf']").val();
  userId = $("#id").val();
  params = { id: userId, email: userEmail, _csrf: csrfValue };
  $.post(url, params, function (response) {
    if (response == "OK") {
      form.submit();
    } else if (response == "Duplicated") {
      showModalDialog(
        "Warning",
        "There is another user having the same email " + userEmail
      );
    } else {
      showModalDialog("Error", "Unknown response from server");
    }
  }).fail(function () {
    showModalDialog("Error", "Could not connect to the server");
  });
  return false;
}

// Show modal dialog box
function showModalDialog(title, message) {
  $("#modalTitle").text(title);
  $("#modalBody").text(message);
  $("#modalDialog").modal();
}

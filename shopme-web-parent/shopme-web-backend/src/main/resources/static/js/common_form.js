// Cancel button
$(document).ready(function () {
  $("#buttonCancel").on("click", function () {
    window.location = moduleUrl;
  });

  //Check file size before showing thumbnail
  $("#fileImage").change(function () {
    fileSize = this.files[0].size;
    if (fileSize > 102400) {
      this.setCustomValidity("You must choose an image less than 100 KB");
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

function showModalDialog(title, message) {
  $("#modalTitle").text(title);
  $("#modalBody").text(message);
  $("#modalDialog").modal();
}

function showErrorModal(message) {
  showModalDialog("Error", message);
}

function showWarningModal(message) {
  showModalDialog("Warning", message);
}

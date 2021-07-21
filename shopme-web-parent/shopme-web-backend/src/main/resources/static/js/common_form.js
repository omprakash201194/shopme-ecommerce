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
  console.log("Showing thumbnail image..");
  var file = fileInput.files[0];
  console.log(file);
  var reader = new FileReader();
  reader.onload = function (e) {
    console.log($("#thumbnail").attr("src"));
    console.log(e.target.result);
    $("#thumbnail").attr("src", e.target.result);
  };
  console.log($("#thumbnail").attr("src"));
  reader.readAsDataURL(file);
}

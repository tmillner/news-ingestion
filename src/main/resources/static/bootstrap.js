  /** Set up date picker **/
  var options = {

    showButtonPanel: true,
    dateFormat: "yy-mm-dd",
    altFormat: "yy-mm-dd",
    showOn: "button",
    buttonImage: "images/calendar.png",
    buttonImageOnly: true,
    buttonText: "Select date",
    showAnim: "drop",
    autoSize: true

  };

  $(function() {
    $(".datepicker").width(120).datepicker(options);
  });

  /** Default initializations **/
  $(function() {
    $("#from_datepicker").datepicker("setDate", "-31d");
    $("#to_datepicker").datepicker("setDate", "-0d");
  });

  /** Set up checkbox functionality **/
  $(function() {
    $("input[type='radio']").checkboxradio({
      icon: false
    });
  });

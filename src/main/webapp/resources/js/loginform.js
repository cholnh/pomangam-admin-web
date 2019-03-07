(function($) {
    "use strict";
	
	// Options for Message
	//----------------------------------------------
  var options = {
	  'btn-loading': '<i class="fa fa-spinner fa-pulse"></i>',
	  'btn-success': '<i class="fa fa-check"></i>',
	  'btn-error': '<i class="fa fa-remove"></i>',
	  'msg-success': 'All Good! Redirecting...',
	  'msg-error': 'Wrong login credentials!',
	  'useAJAX': true,
  };

	// Login Form
	//----------------------------------------------
	// Validation
  $("#login-form").validate({
  	rules: {
      user: "required",
  	  password: "required",
    },
  	errorClass: "form-invalid"
  });
  
	// Form Submission
  $("#login-form").submit(function() {
  	remove_loading($(this));
		
		if(options['useAJAX'] == true)
		{
			// Dummy AJAX request (Replace this with your AJAX code)
		  // If you don't want to use AJAX, remove this
  	  dummy_submit_form($(this));
		
		  // Cancel the normal submission.
		  // If you don't want to use AJAX, remove this
  	  return false;
		}
  });

  $("#register-form").validate({
	  	rules: {
	      reg_username: "required",
	  	  reg_password: {
	  			required: true,
	  			minlength: 5
	  		},
	   		reg_password_confirm: {
	  			required: true,
	  			minlength: 5,
	  			equalTo: "#register-form [name=reg_password]"
	  		},
	  		reg_email: {
	  	    required: true,
	  			email: true
	  		},
	  		reg_agree: "required",
	    },
		  errorClass: "form-invalid",
		  errorPlacement: function( label, element ) {
		    if( element.attr( "type" ) === "checkbox" || element.attr( "type" ) === "radio" ) {
	    		element.parent().append( label ); // this would append the label after all your checkboxes/labels (so the error-label will be the last element in <div class="controls"> )
		    }
				else {
	  	  	label.insertAfter( element ); // standard behaviour
	  	  }
	    }
	  });

	  // Form Submission
	  $("#register-form").submit(function() {
	  	remove_loading($(this));
			
			if(options['useAJAX'] == true)
			{
				// Dummy AJAX request (Replace this with your AJAX code)
			  // If you don't want to use AJAX, remove this
	  	  dummy_submit_form($(this));
			
			  // Cancel the normal submission.
			  // If you don't want to use AJAX, remove this
	  	  return false;
			}
	  });
  
	// Loading
	//----------------------------------------------
  function remove_loading($form)
  {
  	$form.find('[type=submit]').removeClass('error success');
  	$form.find('.login-form-main-message').removeClass('show error success').html('');
  }

  function form_loading($form)
  {
    $form.find('[type=submit]').addClass('clicked').html(options['btn-loading']);
  }
  
  function form_success($form)
  {
	  $form.find('[type=submit]').addClass('success').html(options['btn-success']);
	  $form.find('.login-form-main-message').addClass('show success').html(options['msg-success']);
  }

  function form_failed($form)
  {
  	$form.find('[type=submit]').addClass('error').html(options['btn-error']);
  	$form.find('.login-form-main-message').addClass('show error').html(options['msg-error']);
  }

	// Dummy Submit Form (Remove this)
	//----------------------------------------------
	// This is just a dummy form submission. You should use your AJAX function or remove this function if you are not using AJAX.
  function dummy_submit_form($form)
  {
  	if($form.valid())
  	{
  		
  		setTimeout(function() {
  			form_loading($form);
  		}, 1000);
  		form_success($form);
  		setTimeout(function() {
  			
  			var username = document.getElementById("username").value;
  		    var password = document.getElementById("password").value;
  		    try {
  		    	
  		    	var isRemember = $("#ob-remember").is(":checked");
  		    	/*
  		    	if(isRemember){
		            var userId = $("#username").val();
		            setCookie("cookieUsername", userId, 7); // 7일동안 쿠키 보관
		            
		        } else {
		            deleteCookie("cookieUsername");
		        }
		        */
  		        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
  		        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
  		        submitEncryptedForm(username,password, rsaPublicKeyModulus, rsaPublicKeyExponent, isRemember);
  		    } catch(err) {
  		        alert(err);
  		    }
  		}, 1000);
  	}
  }
	function submitEncryptedForm(username, password, rsaPublicKeyModulus, rsaPpublicKeyExponent, isRemember) {
	    var rsa = new RSAKey();
	    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);
	
	    // 사용자ID와 비밀번호를 RSA로 암호화한다.
	    var securedUsername = rsa.encrypt(username);
	    var securedPassword = rsa.encrypt(password);
	
	    // POST 로그인 폼에 값을 설정하고 발행(submit) 한다.
	    var securedLoginForm = document.getElementById("securedLoginForm");
	    securedLoginForm.securedUsername.value = securedUsername;
	    securedLoginForm.securedPassword.value = securedPassword;
	    securedLoginForm.remember.value = isRemember;
	    securedLoginForm.submit();
	}
	
})(jQuery);
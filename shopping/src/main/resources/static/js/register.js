

		$(document).ready(function(){
			function isEmail(emailStr){
				var patternEmail = /^[a-zA-Z0-9_\.]{5,32}@[a-z0-9]{2,}(\.[a-z0-9]{2,4})$/;
				return patternEmail.test(emailStr);
			}
			function isUsername(nameStr){
				var patternName = /^[a-zA-Z0-9]{4,15}$/;
				return patternName.test(nameStr);
			}
			$('#form-register').submit(function(event){
				var username = $.trim($('#name').val());
				var password = $.trim($('#pass').val());
				var repeatPass = $.trim($('#repeatPass').val());
				var email = $.trim($('#email').val());
				var phone = $.trim($('#phone').val());
				var address = $.trim($('#address').val());
				
				var check = true;
				/*tên đăng nhập lớn hơn 4 ký tự*/
				if(username == '' || !isUsername(username)){
					$('#name_error').text("Tên đăng nhập từ 4 đến 15 ký tự");
					event.preventDefault();
					return false;
				}else{
					$('#name_error').text('').show();
				}

				/*Mật khẩu không được để trống*/
				if(password == ''){
					$('#pass_error').text("Mật khẩu không được để trống!").show();
					event.preventDefault();
					return false;
				}else{
					$('#pass_error').text('').show();
				}

				/*Mật khẩu nhập lại phải giống mật khẩu ở trên*/
				if(repeatPass == ''){
					$('#repeatPass_error').text("Mật khẩu nhập lại không được để trống").show();
					event.preventDefault();
					return false;
				}else if(repeatPass != password){
					$('#repeatPass_error').text("Mật khẩu nhập lại phải trùng với mật khẩu").show();
					return false;
				}else{
					$('#repeatPass_error').text("").show;
					
				}

				/*Email không được để trống và phải đúng định dạng*/
				if (email == '' || !isEmail(email)) {
					$('#email_error').text("Email không được để trống hoặc không đúng định dạng").show();
					event.preventDefault();
					return false;
				}else{
					$('#email_error').text('').show();
				}
			});
		});

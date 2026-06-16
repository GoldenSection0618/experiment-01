export const usernameRules = [
  { required: true, message: '用户名不能为空', trigger: 'blur' }
];

export const passwordRules = [
  { required: true, message: '密码不能为空', trigger: 'blur' }
];

export const emailRules = [
  { required: true, message: '邮箱不能为空', trigger: 'blur' },
  { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
];

export const birthdayRules = [
  { required: true, message: '请选择出生年月', trigger: 'change' }
];

export function confirmPasswordRules(getPassword) {
  return [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== getPassword()) {
          callback(new Error('两次密码不一致'));
          return;
        }
        callback();
      },
      trigger: 'blur'
    }
  ];
}

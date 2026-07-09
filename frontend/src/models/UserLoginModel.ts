export interface UserLogin {
  username: string;
  password: string;
}

export interface UserLoginResponse {
  token: string;
  user: any;
}

export const UserLoginModel = (): UserLogin => ({
  username: '',
  password: '',
});

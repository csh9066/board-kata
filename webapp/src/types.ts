export interface UserInfo {
  id: number;
  email: string;
  nickname: string;
}

export interface CreateUserData {
  email: string;
  password: string;
  nickname: string;
}

export interface ChangePasswordData {
  password: string;
  newPassword: string;
  confirmPassword: string;
}

export interface UserResponse {
  id: number;
  name: string;
  email: string;
}

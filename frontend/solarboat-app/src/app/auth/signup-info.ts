import { RoleType } from "../model/role-type.enum";

export class SignUpInfo {
  name: string;
  username: string;
  email: string;
  role: string[];
  password: string;

  constructor(
    name: string,
    username: string,
    email: string,
    password: string,
    admin: boolean,
    editor: boolean
  ) {
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = [RoleType.User];
    if (admin) {
      this.role.push(RoleType.Admin);
    }
    if(editor){
      this.role.push(RoleType.Editor);
    }
  }
}

import { Role } from './role';
import { RoleType } from './role-type.enum';

export class User {
  id: number;
  name: string;
  username: string;
  email: string;
  password: string;
  roles: RoleType[];

  constructor() {}
}

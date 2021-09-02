import { Component, OnInit } from "@angular/core";
import { UserService } from "../../shared/user.service";
import { User } from "../../model/user";
import { NgbModal, ModalDismissReasons } from "@ng-bootstrap/ng-bootstrap";
import { Role } from "../../model/role";
import { RoleType } from "src/app/model/role-type.enum";

@Component({
  selector: "app-users",
  templateUrl: "./users.component.html",
  styleUrls: ["./users.component.css"],
})
export class UsersComponent implements OnInit {
  title = "appBootstrap";
  closeResult: string;
  constructor(user: UserService, private modalService: NgbModal) {
    this.userService = user;
  }
  users: User[];
  userService: UserService;
  selectedUser: User;
  originalUser: User;
  editing = false;
  isAdmin: boolean;
  isEditor: boolean;
  ngOnInit(): void {
    this.loadUsers();
  }
  edit(user: User) {
    this.editing = true;
    // console.log(user);
  }
  open(content, id: number) {
    console.log(this.users);
    var selectedUserTmp = this.users.find((t) => t.id == id);
    this.selectedUser = { ...selectedUserTmp };

    this.isAdmin = this.selectedUser.roles.indexOf(RoleType.Admin) != -1;
    this.isEditor = this.selectedUser.roles.indexOf(RoleType.Editor) != -1;
    console.log(this.selectedUser.roles.indexOf(RoleType.Admin) != -1);
    console.log(this.selectedUser.roles.indexOf(RoleType.Editor) != -1);
    console.log(this.selectedUser.roles);

    this.modalService.open(content).result.then(
      (result) => {
        this.closeResult = `Closed with: ${result}`;
      },
      (reason) => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }
  submit() {
    // this.selectedUser.roles = [new Role("ROLE_USER")];
    // if (this.selectedUser.isAdmin) {
    //   this.selectedUser.roles.push(new Role("ROLE_ADMIN"));
    // }
    // if (this.selectedUser.isEditor) {
    //   this.selectedUser.roles.push(new Role("ROLE_EDITOR"));
    // }
    this.selectedUser.roles = [RoleType.User];
    if (this.isAdmin) {
      this.selectedUser.roles.push(RoleType.Admin);
    }
    if (this.isEditor) {
      this.selectedUser.roles.push(RoleType.Editor);
    }

    //this.originalUser = this.selectedUser;
    this.userService
      .updateUser(this.selectedUser)
      .subscribe((error) => console.log(error));
  }
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return "by pressing ESC";
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return "by clicking on a backdrop";
    } else {
      return `with: ${reason}`;
    }
  }
  loadUsers() {
    this.userService.getUsers().subscribe((t) => {
      this.users = [];
      console.log(t);
      t.forEach((u) => {
        var user = new User();
        user.roles = [];
        user.email = u.email;
        user.id = u.id;
        user.password = u.password;
        user.username = u.username;
        user.name = u.name;
        u.roles.forEach((r) => {
          console.log(r.name);
          user.roles.push(r.name);
        });
        console.log(user);
        this.users.push(user);
      });
    });
  }

  close() {
    this.modalService.dismissAll();
  }
  confirmDelete(id: number, name: string) {
    if (confirm("Biztos hogy törlöd a(z) " + name + "felhasználót?")) {
      this.delete(id);
    }
  }
  delete(id: number) {
    this.userService.deleteUser(id).subscribe(
      (data) => {
        var du = this.users.find((a) => a.id == id);
        const index = this.users.indexOf(du, 0);
        if (index > -1) {
          this.users.splice(index, 1);
        }
      },
      (error) => {
        // console.log(error);
      }
    );
  }
}

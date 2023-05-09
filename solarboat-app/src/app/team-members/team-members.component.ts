import {Component, Input, OnInit} from '@angular/core';
import {Member} from '../model/member';
import {Globals} from "../globals";

@Component({
  selector: 'app-team-members',
  templateUrl: './team-members.component.html',
  styleUrls: ['./team-members.component.css']
})
export class TeamMembersComponent implements OnInit {
  @Input() members: Member[];
  @Input() leader: Member;
  @Input() isLeader: boolean;
  TEAM_URL_BASE = '';
  constructor(private globals: Globals) {}

  ngOnInit(): void {
    this.TEAM_URL_BASE = this.globals.IMG_ROUTE + "/members";
    const elements = document.querySelectorAll('.animate-me');
    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.intersectionRatio > 0) {
          entry.target.classList.add('animated');
        } else {
          // entry.target.classList.remove('animated');
        }
      });
    });
    elements.forEach(el => {
      observer.observe(el);
    });
  }

}

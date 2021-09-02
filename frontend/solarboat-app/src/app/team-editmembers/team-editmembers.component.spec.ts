import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamEditmembersComponent } from './team-editmembers.component';

describe('TeamEditmembersComponent', () => {
  let component: TeamEditmembersComponent;
  let fixture: ComponentFixture<TeamEditmembersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeamEditmembersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamEditmembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

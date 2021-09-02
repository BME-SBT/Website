import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamEditgroupsComponent } from './team-editgroups.component';

describe('TeamEditgroupsComponent', () => {
  let component: TeamEditgroupsComponent;
  let fixture: ComponentFixture<TeamEditgroupsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeamEditgroupsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamEditgroupsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

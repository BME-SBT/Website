import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamEditgroupPanelComponent } from './team-editgroup-panel.component';

describe('TeamEditgroupPanelComponent', () => {
  let component: TeamEditgroupPanelComponent;
  let fixture: ComponentFixture<TeamEditgroupPanelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeamEditgroupPanelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamEditgroupPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamUserviewComponent } from './team-userview.component';

describe('TeamUserviewComponent', () => {
  let component: TeamUserviewComponent;
  let fixture: ComponentFixture<TeamUserviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeamUserviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamUserviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

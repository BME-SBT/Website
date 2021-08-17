import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PressPreviewComponent } from './press-preview.component';

describe('PressPreviewComponent', () => {
  let component: PressPreviewComponent;
  let fixture: ComponentFixture<PressPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PressPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PressPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

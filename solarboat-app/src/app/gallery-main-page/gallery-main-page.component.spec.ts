import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GalleryMainPageComponent } from './gallery-main-page.component';

describe('GalleryMainPageComponent', () => {
  let component: GalleryMainPageComponent;
  let fixture: ComponentFixture<GalleryMainPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GalleryMainPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GalleryMainPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

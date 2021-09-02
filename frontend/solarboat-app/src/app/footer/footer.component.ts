import { Component, OnInit, ViewEncapsulation } from '@angular/core';
// import AOS from 'aos';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
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
    // AOS.init();
  }

}

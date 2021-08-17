package hu.schdesign.solarboat.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class ViewController{
        @RequestMapping(value = "/news", method = RequestMethod.GET)
        public ModelAndView method() {
            return new ModelAndView("redirect:" + "www.google.com");
        }
}

package punto_venta.sombrilla_verde.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "vistas/login";
    }
}

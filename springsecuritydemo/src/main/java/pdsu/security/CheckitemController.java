package pdsu.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkitem")
public class CheckitemController {

    @RequestMapping("/find")
    public String find(){
        return "find";
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")
    public String add(){
        return "add";
    }


    @RequestMapping("/del")
    @PreAuthorize("hasRole('ROLE_admin')")
    public String del(){
        return "del";
    }
}

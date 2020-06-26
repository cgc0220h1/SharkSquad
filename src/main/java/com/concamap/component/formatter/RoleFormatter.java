package com.concamap.component.formatter;

import com.concamap.model.Roles;
import com.concamap.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class RoleFormatter implements Formatter<Roles> {
    private final RoleService roleService;

    @Autowired
    public RoleFormatter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Roles parse(String id, Locale locale) throws ParseException {
        return roleService.findExistById(Integer.parseInt(id));
    }

    @Override
    public String print(Roles object, Locale locale) {
        return null;
    }
}

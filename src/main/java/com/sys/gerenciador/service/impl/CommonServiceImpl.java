package com.sys.gerenciador.service.impl;

import com.sys.gerenciador.service.ICommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Objects;

@Service
public class CommonServiceImpl implements ICommonService {

    @Override
    public void removeSessionMessage() {
        HttpServletRequest request = ((ServletRequestAttributes) (Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())))
                .getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("succMsg");
        session.removeAttribute("errorMsg");
    }
}

package com.warehouse.bear.management.filter;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.service.WarehouseUserDetailsService;
import com.warehouse.bear.management.utils.WarehouseJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WarehouseFilter extends OncePerRequestFilter {

    @Autowired
    private WarehouseJwtUtil warehouseJwtUtil;

    @Autowired
    private WarehouseUserDetailsService warehouseUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(WarehouseUserConstants.WAREHOUSE_AUTHORIZATION);

        String token = null;
        String userName = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(WarehouseUserConstants.WAREHOUSE_HEADER)) {
            token = authorizationHeader.substring(7);
            userName = warehouseJwtUtil.extractUsername(token);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = warehouseUserDetailsService.loadUserByUsername(userName);

            if (warehouseJwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

package br.com.jsa.carteiralegal.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jsa.carteiralegal.service.UsuarioService;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("===========================  I N Í C I O  ===========================");
		System.out.println("Request URI  : "+ request.getRequestURI());
		System.out.println("Method       : "+ request.getMethod());
		System.out.println("content type : "+request.getHeader("Content-type"));
		System.out.println("Authorization: "+request.getHeader("Authorization"));
		String username = null;
		String jwtToken = null;
		
		// JWT Token está no form "Bearer token". Remova a palavra Bearer e pegue
		// somente o Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer") && requestTokenHeader.split("#").length >= 2) {
			jwtToken = requestTokenHeader.split("#")[1];
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// Tendo o token, valide o.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.usuarioService.dadosAutenticacaoAutorizacao(username);

			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		System.out.println("STATUS CODE: "+response.getStatus());
		System.out.println("=============================   F I M   =============================");
		chain.doFilter(request, response);
	}

}

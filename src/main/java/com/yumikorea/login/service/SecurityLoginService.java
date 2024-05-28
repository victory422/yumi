package com.yumikorea.login.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yumikorea.admin.dto.SecurityAdminResponseDto;
import com.yumikorea.admin.entity.Admin;
import com.yumikorea.admin.repository.AdminRepository;
import com.yumikorea.admin.repository.AdminRepositoryCustom;
import com.yumikorea.common.enums.EAdminConstants;
import com.yumikorea.common.enums.EAdminMessages;
import com.yumikorea.common.utils.CommonUtil;
import com.yumikorea.common.utils.PwHashUtils;
import com.yumikorea.login.dto.LoginRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SecurityLoginService implements UserDetailsService {

	@Autowired
	private final AdminRepository adminRepository;
	
	@Autowired
	private final AdminRepositoryCustom adminRepositoryCustom;

	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private HttpServletRequest request;

	private final Logger logger = LoggerFactory.getLogger(SecurityLoginService.class);

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		if( loginId != null ) loginId = loginId.trim();
		Optional<Admin> opt = adminRepository.findById(loginId);
		
		if (opt == null) throw new UsernameNotFoundException(
				EAdminMessages.CANNOT_FIND_USER.getMessage());
		
		SecurityAdminResponseDto responseDto = new SecurityAdminResponseDto(opt.get());
		
		
		String sessionId = (String) request.getSession().getId();
		String confirmLogout = request.getParameter(EAdminConstants.CONFIRM_LOGOUT.getValue());
		String dbpw = responseDto.getPassword();

		// 비밀번호 체크
		String checkPassword = CommonUtil.encodePasswordSecurity(dbpw, sessionId);
		String authVal = "";
		if (responseDto.getAuth() == 0) {
			authVal = EAdminConstants.ADMIN.getValue();
		} else if (responseDto.getAuth() == 1) {
			authVal = EAdminConstants.USER.getValue();
		}
		if( !responseDto.getIsEnabled() ) {
			logger.info("========================ADMIN.ENABLE_TYPE is N. Login not permitted.========================");
		}
		Boolean accountLocked = false; // 계정잠금
		// 02 : 초기비밀번호 세팅 , 03 : 카운트오버
		String userState = responseDto.getUserState();
		if (userState.equals("03")) {
			accountLocked = true;
		} else if (userState.equals("02")) {
			// disabled = true;
			authVal = EAdminConstants.INIT.getValue();
		}
		String[] role = { authVal };
		logger.info("login role is : " + role[0]);
		if (Boolean.parseBoolean(confirmLogout)) {
			// 타 사용자 세션인증 제거
			this.expireSession(loginId);
		}
		
		SecurityAdminResponseDto resultDVO = new SecurityAdminResponseDto(
				(User) User.builder().username(loginId).password(checkPassword)
				.roles(role).accountLocked(accountLocked).disabled(!responseDto.getIsEnabled()).build());
		logger.info("login VO : " + resultDVO.toString());
		return resultDVO;
	}

	/* 최초 로그인 - 초기 비밀번호 설정 */
	public Map<String, String> updateInitPw(LoginRequestDto requestDto, String sessionId) {
		Map<String, String> map = new HashMap<>();
		String dbpw = adminRepository.findById(requestDto.getLoginId()).get().getPassword();
		try {
			/* 현재 비밀번호 == DB에 저장된 비밀번호 일치할 경우 */
			if (CommonUtil.encodePassword(dbpw, sessionId).equals(requestDto.getOrgPw())) {
				String newPw = PwHashUtils.getPwHash(requestDto.getNewPw(), requestDto.getLoginId());
				adminRepositoryCustom.updatePassword(requestDto.getLoginId(), newPw);
				map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.SUCCESS.getValue());
				map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.PASSWORD_CHANGE_SUCCESS.getMessage());
			} else {
				map.put(EAdminConstants.STATUS.getValue(), EAdminConstants.FAIL.getValue());
				map.put(EAdminConstants.MESSAGE.getValue(), EAdminMessages.PASSWORD_CHANGE_FAIL.getMessage());
				throw new UsernameNotFoundException(EAdminMessages.ILLEGAL_PASSWORD.getMessage());
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			// 기존 로그인 시도시 저장된 세션을 제거
			expireSession(requestDto.getLoginId());
		}
		return map;
	}

	// 현재 로그인된 사용자를 로그아웃시킴
	private void expireSession(String loginId) {
		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
		// 각 Principal(사용자)에 대한 세션 정보를 가져옵니다.
		for (Object principal : allPrincipals) {
			if (principal instanceof UserDetails) {
				SecurityAdminResponseDto userDetails = (SecurityAdminResponseDto) principal;
				// 현재 사용자의 모든 세션 정보를 가져옵니다.
				if( userDetails.getLoginId().equals(loginId)) {
					List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
					for (SessionInformation s : sessions) {
						SessionInformation session = sessionRegistry.getSessionInformation(s.getSessionId());
						if ( session != null && session.isExpired() == false ) {
							logger.info("[expired session] "+userDetails.getLoginId() + " : " + s.getSessionId());
							session.expireNow();
							sessionRegistry.removeSessionInformation(s.getSessionId());
						}
					}
					break;
				}
			}
		}
	}
	
}

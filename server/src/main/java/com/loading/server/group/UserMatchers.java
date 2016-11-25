package com.loading.server.group;

import com.loading.server.user.AbstractExtendUser;

public class UserMatchers {
	
	@SuppressWarnings("rawtypes")
	public static final UserMatcher MATCHER_ALL = new UserMatcher() {
		@Override
		public boolean matches(AbstractExtendUser user) {
			return true;
		}
	};

}

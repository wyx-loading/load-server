package com.loading.server.group;

import com.loading.server.user.AbstractExtendUser;

public interface UserMatcher<T extends AbstractExtendUser> {
	
	boolean matches(T user);

}

/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wangshuwei5.util;

/**
 * @author Lilinfeng
 * @date 2014年3月15日
 * @version 1.0
 */
public enum MessageType {

	SERVICE_REQ(0),
	SERVICE_RESP(1),
	ONE_WAY(2),
	LOGIN_REQ(3),
	LOGIN_RESP(4),
	HEARTBEAT_REQ(5),
	HEARTBEAT_RESP(6);

	private int value;

	private MessageType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}

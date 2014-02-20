package com.persado.oss.quality.stevia.selenium.core.controllers.commonapi;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado
 * %%
 * Copyright (c) Persado Intellectual Property Limited. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *  
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  
 * * Neither the name of the Persado Intellectual Property Limited nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */


/** Enumeration that provides common handling in Keys, Between WebDriver and Selenium RC
 * Web Driver will use the mapped key, and RC will use the mapped Event.
 */

import org.openqa.selenium.Keys;
import java.awt.event.*;

public enum KeyInfo {
	

	  CANCEL       (Keys.CANCEL,KeyEvent.VK_CANCEL), 
	  HELP         (Keys.HELP,KeyEvent.VK_HELP),
	  BACK_SPACE   (Keys.BACK_SPACE,KeyEvent.VK_BACK_SPACE),
	  TAB          (Keys.TAB,KeyEvent.VK_TAB),
	  CLEAR        (Keys.CLEAR,KeyEvent.VK_CLEAR),
	  RETURN       (Keys.RETURN,KeyEvent.VK_ENTER),
	  ENTER        (Keys.ENTER,KeyEvent.VK_ENTER),
	  SHIFT        (Keys.SHIFT,KeyEvent.VK_SHIFT),
	  LEFT_SHIFT   (Keys.LEFT_SHIFT,KeyEvent.VK_SHIFT), 
	  CONTROL      (Keys.CONTROL,KeyEvent.VK_CONTROL),
	  LEFT_CONTROL (Keys.LEFT_CONTROL,KeyEvent.VK_CONTROL),
	  ALT          (Keys.ALT,KeyEvent.VK_ALT),
	  LEFT_ALT     (Keys.LEFT_ALT,KeyEvent.VK_ALT),
	  PAUSE        (Keys.PAUSE,KeyEvent.VK_PAUSE),
	  ESCAPE       (Keys.ESCAPE,KeyEvent.VK_ESCAPE),
	  SPACE        (Keys.SPACE,KeyEvent.VK_SPACE),
	  PAGE_UP      (Keys.PAGE_UP,KeyEvent.VK_PAGE_UP),
	  PAGE_DOWN    (Keys.PAGE_DOWN,KeyEvent.VK_PAGE_DOWN),
	  END          (Keys.END,KeyEvent.VK_END),
	  HOME         (Keys.HOME,KeyEvent.VK_HOME),
	  LEFT         (Keys.LEFT,KeyEvent.VK_LEFT),
	  ARROW_LEFT   (Keys.ARROW_LEFT,KeyEvent.VK_LEFT),
	  UP           (Keys.UP,KeyEvent.VK_UP),
	  ARROW_UP     (Keys.ARROW_UP,KeyEvent.VK_UP),
	  RIGHT        (Keys.RIGHT,KeyEvent.VK_RIGHT),
	  ARROW_RIGHT  (Keys.ARROW_RIGHT,KeyEvent.VK_RIGHT), 
	  DOWN         (Keys.DOWN,KeyEvent.VK_DOWN),
	  ARROW_DOWN   (Keys.ARROW_DOWN,KeyEvent.VK_DOWN), 
	  INSERT       (Keys.INSERT,KeyEvent.VK_INSERT),
	  DELETE       (Keys.DELETE,KeyEvent.VK_DELETE),
	  SEMICOLON    (Keys.SEMICOLON,KeyEvent.VK_SEMICOLON),
	  EQUALS       (Keys.EQUALS,KeyEvent.VK_EQUALS),

	  NUMPAD0      (Keys.NUMPAD0,KeyEvent.VK_NUMPAD0), 
	  NUMPAD1      (Keys.NUMPAD1,KeyEvent.VK_NUMPAD1),
	  NUMPAD2      (Keys.NUMPAD2,KeyEvent.VK_NUMPAD2),
	  NUMPAD3      (Keys.NUMPAD3,KeyEvent.VK_NUMPAD3),
	  NUMPAD4      (Keys.NUMPAD4,KeyEvent.VK_NUMPAD4),
	  NUMPAD5      (Keys.NUMPAD5,KeyEvent.VK_NUMPAD5),
	  NUMPAD6      (Keys.NUMPAD6,KeyEvent.VK_NUMPAD6),
	  NUMPAD7      (Keys.NUMPAD7,KeyEvent.VK_NUMPAD7),
	  NUMPAD8      (Keys.NUMPAD8,KeyEvent.VK_NUMPAD8),
	  NUMPAD9      (Keys.NUMPAD9,KeyEvent.VK_NUMPAD9),
	  MULTIPLY     (Keys.MULTIPLY,KeyEvent.VK_MULTIPLY),
	  ADD          (Keys.ADD,KeyEvent.VK_ADD),
	  SEPARATOR    (Keys.SEPARATOR,KeyEvent.VK_SEPARATOR),
	  SUBTRACT     (Keys.SUBTRACT,KeyEvent.VK_SUBTRACT),
	  DECIMAL      (Keys.DECIMAL,KeyEvent.VK_DECIMAL),
	  DIVIDE       (Keys.DIVIDE,KeyEvent.VK_DIVIDE),

	  F1           (Keys.F1,KeyEvent.VK_F1),  
	  F2           (Keys.F2,KeyEvent.VK_F2),
	  F3           (Keys.F3,KeyEvent.VK_F3),
	  F4           (Keys.F4,KeyEvent.VK_F4),
	  F5           (Keys.F5,KeyEvent.VK_F5),
	  F6           (Keys.F6,KeyEvent.VK_F6),
	  F7           (Keys.F7,KeyEvent.VK_F7),
	  F8           (Keys.F8,KeyEvent.VK_F8),
	  F9           (Keys.F9,KeyEvent.VK_F9),
	  F10          (Keys.F10,KeyEvent.VK_F10),
	  F11          (Keys.F11,KeyEvent.VK_F11),
	  F12          (Keys.F12,KeyEvent.VK_F12),
	  
	  META         (Keys.META,KeyEvent.VK_META),
	  COMMAND      (Keys.COMMAND,KeyEvent.VK_META),
	  ;
	
	
	private Keys mappedKey;
	private int mappedEvent;
	
	KeyInfo(Keys mapThisKey, int toThisEvent ) {
		mappedKey = mapThisKey;
		mappedEvent = toThisEvent;
	}
	
	
	public Keys getKey() {
		return mappedKey;
	}
	
	public String getEvent() {
		return String.valueOf(mappedEvent);
	}
	
	public String toString() {
		return "KeyInfo[mappedKey="+mappedKey+" is mapped to event "+mappedEvent+"]"; 
	}
}

/*
 * Copyright (c) 2015, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uni.freiburg.iig.telematik.wolfgang.actions.help;

/**
 *
 * @author julius
 */
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedList;

public class ErrorStorage {
	static ErrorStorage myErrorStorage = new ErrorStorage();
	private ErrorStore store = new ErrorStore();
	private final String email = "swat@iig.uni-freiburg.de";

	public static void main(String args[]) throws IOException, URISyntaxException {
		ErrorStorage eStore = ErrorStorage.getInstance();
		eStore.addMessage(null, new Exception("String is empty"));
		eStore.addMessage("Exception is empty", null);
		eStore.sendAsMail();
	}


	private ErrorStorage() {
		//which file to use?
	}

	public static ErrorStorage getInstance() {
		return myErrorStorage;
	}
	
	@Override
	public String toString() {
		return store.toString();
	}

	public void addMessage(String message, Exception e) {
		store.addError(message, e);
	}


	public boolean sendAsMail() {
		//first make URI.Compatible
		String errorStrings;
		try {
			errorStrings = URLEncoder.encode(store.toString(), "UTF-8").replace("+", "%20");
			Desktop.getDesktop().mail(new URI("mailto:" + email + "?subject=Bugreport%20WOLFGANG&body=" + errorStrings));
			return true;
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} catch (URISyntaxException e) {
		}
		return false;

	}


	class ErrorElement {

		public ErrorElement(String message, Exception e) {
			this.message = "null";
			if (message != null && !message.isEmpty())
				this.message = message;

			this.e = e;

			this.date = getCalendarString();
		}

		public String toString() {
			String exceptionStackTrace = "null", exceptionMessage = "null", exceptionCause = "null";
			if (e != null) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				exceptionStackTrace = sw.toString();

				try {
					exceptionMessage = e.getMessage();
					exceptionCause = e.getCause().toString();
				} catch (Exception e) {
				}
			}

			return date + ": " + message + " (" + exceptionMessage + ") \r\n" + exceptionCause + "\r\n" + exceptionStackTrace;
		}

		private String getCalendarString() {
			Calendar cal = Calendar.getInstance();
			return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "/"
					+ cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Exception getE() {
			return e;
		}

		public void setE(Exception e) {
			this.e = e;
		}

		private String message;
		private Exception e;
		private String date;
	}

	class ErrorStore {
		LinkedList<ErrorElement> errors = new LinkedList<ErrorElement>();

		public void addError(String message, Exception e) {
			errors.add(new ErrorElement(message, e));
			if (errors.size() > 50)
				errors.removeFirst();
		}

		public String toString(){
			StringBuilder b = new StringBuilder();
			for (ErrorElement elem : errors) {
				b.append(elem.toString());
				b.append("\r\n");
			}
			return b.toString();
		}
	}

}

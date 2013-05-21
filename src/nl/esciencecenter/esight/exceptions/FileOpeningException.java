package nl.esciencecenter.esight.exceptions;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Exception thrown when opening a file in NetCDF fails.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class FileOpeningException extends Exception {

    private static final long serialVersionUID = 0L;

    public FileOpeningException(String message) {
        super(message);
    }

    public FileOpeningException() {
        super();
    }

    public FileOpeningException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileOpeningException(Throwable cause) {
        super(cause);
    }
}

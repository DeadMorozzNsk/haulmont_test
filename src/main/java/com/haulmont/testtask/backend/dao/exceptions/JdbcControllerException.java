package com.haulmont.testtask.backend.dao.exceptions;

public class JdbcControllerException extends Exception {
    /**
     * Constructs a <code>JdbcControllerException</code> object with a given
     * <code>reason</code>. The  <code>SQLState</code>  is initialized to
     * <code>null</code> and the vendor code is initialized to 0.
     * <p>
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method.
     * <p>
     *
     * @param reason a description of the exception
     */
    public JdbcControllerException(String reason) {
        super(reason);
    }

    /**
     * Constructs a <code>JdbcControllerException</code> object.
     * The <code>reason</code>, <code>SQLState</code> are initialized
     * to <code>null</code> and the vendor code is initialized to 0.
     * <p>
     * The <code>cause</code> is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method.
     */
    public JdbcControllerException() {
    }

    /**
     * Constructs a <code>JdbcControllerException</code> object with a given
     * <code>cause</code>.
     * The <code>SQLState</code> is initialized
     * to <code>null</code> and the vendor code is initialized to 0.
     * The <code>reason</code>  is initialized to <code>null</code> if
     * <code>cause==null</code> or to <code>cause.toString()</code> if
     * <code>cause!=null</code>.
     * <p>
     *
     * @param cause the underlying reason for this <code>SQLException</code>
     *              (which is saved for later retrieval by the <code>getCause()</code> method);
     *              may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public JdbcControllerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a <code>JdbcControllerException</code> object with a given
     * <code>reason</code> and  <code>cause</code>.
     * The <code>SQLState</code> is  initialized to <code>null</code>
     * and the vendor code is initialized to 0.
     * <p>
     *
     * @param reason a description of the exception.
     * @param cause  the underlying reason for this <code>SQLException</code>
     *               (which is saved for later retrieval by the <code>getCause()</code> method);
     *               may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public JdbcControllerException(String reason, Throwable cause) {
        super(reason, cause);
    }
}

package site.itprohub.javelin.data.command;

@FunctionalInterface
public interface StatementCallback<T> {

    T execute() throws Exception;
}

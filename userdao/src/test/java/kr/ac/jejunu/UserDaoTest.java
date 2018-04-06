package kr.ac.jejunu;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.*;  // 테스트할때 hamcrest 라이브러리를 많이 쓴다
import static org.hamcrest.MatcherAssert.*;  // static 을 붙이면 static 메소드를 쓸 수 있다


public class UserDaoTest {

    private UserDao userDao; // 제주대는 그대로 UserDao 사용
    private DaoFactory daoFactory;

    @Before
    public void setup(){
//        daoFactory = new DaoFactory();
//        userDao = daoFactory.getUserDao();
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = applicationContext.getBean("userDao", UserDao.class);
    }

    @Test
    public void get() throws SQLException, ClassNotFoundException{
        // Exception 처리는 잘 알때 사용, 확신이 없을 땐 알아서 하게 내버려 둬야한다
        // 내가 처리할 수 없는 Exception 은 throw 하는게 맞다.
        int id = 1;
        User user = userDao.get(id);
        assertThat(user.getId(), is(1));
        assertThat(user.getName(), is("song"));
        assertThat(user.getPassword(), is("1234"));
    }

    @Test
    public void add() throws SQLException, ClassNotFoundException{
        User user = new User();
        Integer id = insertUserTest(user);

        User insertedUser = userDao.get(id);
        assertThat(insertedUser.getId(), is(id)); // inserted 된 id  가 위에있는 user 의 Id 와 같는지 판별
        assertThat(insertedUser.getName(), is(user.getName()));
        assertThat(insertedUser.getPassword(), is(user.getPassword()));
    }

    @Test
    public void update() throws SQLException, ClassNotFoundException {
        User user = new User();
        Integer id = insertUserTest(user);

        user.setId(id);
        user.setName("update");
        user.setPassword("1234");
        userDao.update(user);

        User updatedUser = userDao.get(id);

        assertThat(updatedUser.getId(), is(user.getId()));
        assertThat(updatedUser.getName(), is(user.getName()));
        assertThat(updatedUser.getPassword(), is(user.getPassword()));
    }

    private Integer insertUserTest(User user) throws SQLException, ClassNotFoundException {
        user.setName("heol");
        user.setPassword("1111");
        return userDao.insert(user);
    }

    @Test
    public void delete() throws SQLException, ClassNotFoundException {
        User user = new User();
        Integer id = insertUserTest(user);

        userDao.delete(id); // id만 받음

        User deletedUser = userDao.get(id);
        assertThat(deletedUser, nullValue()); // 수정했고 삭제하고나서 결과가 널 이어야 함
    }
}
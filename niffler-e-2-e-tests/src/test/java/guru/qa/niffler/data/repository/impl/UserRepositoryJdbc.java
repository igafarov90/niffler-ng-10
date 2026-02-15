package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.FriendshipDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserRepositoryJdbc implements UserRepository {

    private static final UserDao userDao = new UserDaoJdbc();
    private static final FriendshipDao friendshipDao = new FriendshipDaoJdbc();

    @Nonnull
    @Override
    public UserEntity create(UserEntity user) {
        return userDao.create(user);
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userDao.findById(id);
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Nonnull
    @Override
    public UserEntity update(UserEntity user) {
        return userDao.update(user);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        FriendshipEntity friendshipInvitation = new FriendshipEntity();
        friendshipInvitation.setRequester(requester);
        friendshipInvitation.setAddressee(addressee);
        friendshipInvitation.setStatus(FriendshipStatus.PENDING);
        friendshipInvitation.setCreatedDate(new Date());

        friendshipDao.create(friendshipInvitation);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        FriendshipEntity friends = new FriendshipEntity();
        friends.setRequester(requester);
        friends.setAddressee(addressee);
        friends.setStatus(FriendshipStatus.ACCEPTED);
        friends.setCreatedDate(new Date());
        friendshipDao.create(friends);
    }

    @Override
    public void remove(UserEntity user) {
        friendshipDao.delete(user.getId(), user.getId());
        userDao.delete(user);
    }
}

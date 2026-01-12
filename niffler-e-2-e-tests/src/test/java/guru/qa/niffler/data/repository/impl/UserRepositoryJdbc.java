package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.FriendshipDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {
    private final UserDao userDao = new UserDaoJdbc();
    private final FriendshipDao friendshipDao = new FriendshipDaoJdbc();

    @Override
    public UserEntity create(UserEntity user) {
        return userDao.create(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userDao.findById(id);
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        FriendshipEntity incomeInvitation = new FriendshipEntity();
        incomeInvitation.setRequester(requester);
        incomeInvitation.setAddressee(addressee);
        incomeInvitation.setStatus(FriendshipStatus.PENDING);
        incomeInvitation.setCreatedDate(new Date());

        friendshipDao.create(incomeInvitation);
    }

    @Override
    public void addOutComeInvitation(UserEntity requester, UserEntity addressee) {
        FriendshipEntity outComeInvitation = new FriendshipEntity();
        outComeInvitation.setRequester(requester);
        outComeInvitation.setAddressee(addressee);
        outComeInvitation.setStatus(FriendshipStatus.PENDING);
        outComeInvitation.setCreatedDate(new Date());

        friendshipDao.create(outComeInvitation);
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
}

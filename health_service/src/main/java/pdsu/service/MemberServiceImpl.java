package pdsu.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pdsu.dao.MemberDao;
import pdsu.pojo.Member;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl
implements MemberService{

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
}

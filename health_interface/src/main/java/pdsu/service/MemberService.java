package pdsu.service;

import pdsu.pojo.Member;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);
}

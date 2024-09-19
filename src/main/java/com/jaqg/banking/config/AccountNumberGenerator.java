package com.jaqg.banking.config;

import com.jaqg.banking.entity.Account;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class AccountNumberGenerator extends SequenceStyleGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        if (object instanceof Account entity) {
            if (entity.getNumber() == null) {
                return super.generate(session, object);
            }
            return entity.getNumber();
        }
        return super.generate(session, object);
    }
}

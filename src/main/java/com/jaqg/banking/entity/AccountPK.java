package com.jaqg.banking.entity;

import com.jaqg.banking.config.AccountNumberGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AccountPK implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "account-number-gen",
            type = AccountNumberGenerator.class,
            parameters = {
                @Parameter(name = AccountNumberGenerator.INCREMENT_PARAM, value = "50"),
                @Parameter(name = AccountNumberGenerator.INITIAL_PARAM, value = "100000")
            }
            )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account-number-gen")
    @Column(nullable = false)
    @NotNull
    protected Long number;

    @Column(nullable = false)
    @NotNull
    protected Integer sortCode;

    public Long number() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer sortCode() {
        return sortCode;
    }

    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountPK accountPK)) return false;
        return Objects.equals(number, accountPK.number) && Objects.equals(sortCode, accountPK.sortCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, sortCode);
    }
}

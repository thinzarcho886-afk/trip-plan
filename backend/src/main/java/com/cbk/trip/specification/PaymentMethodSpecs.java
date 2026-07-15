package com.cbk.trip.specification;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cbk.trip.entity.PaymentMethod;
import com.cbk.trip.entity.PaymentMethod_; 
import com.cbk.trip.enums.Status;

public class PaymentMethodSpecs {

    // ✅ ပြင်ဆင်ပြီး - Integer accountNumber မှ String accountNumber သို့ ပြောင်းလဲခြင်း
    public static Specification<PaymentMethod> getByFilter(String name, String accountNumber, String accountName, Status status) {
        return new Specification<PaymentMethod>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<PaymentMethod> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final Collection<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(name)) {
                    final Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(PaymentMethod_.NAME)), "%" + name.toLowerCase() + "%");
                    predicates.add(namePredicate);
                }

                // ✅ ပြင်ဆင်ပြီး - accountNumber က String ဖြစ်သွားပြီမို့လို့ .as(String.class) နှင့် .toString() သုံးစရာမလိုတော့ဘဲ တိုက်ရိုက် Like Query စစ်ဆေးနိုင်ပါပြီ
                if (!StringUtils.isEmpty(accountNumber)) {
                    final Predicate accNumPredicate = criteriaBuilder.like(
                            root.get(PaymentMethod_.ACCOUNT_NUMBER), 
                            "%" + accountNumber + "%");
                    predicates.add(accNumPredicate);
                }
                
                if (!StringUtils.isEmpty(accountName)) {
                    final Predicate accNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(PaymentMethod_.ACCOUNT_NAME)), "%" + accountName.toLowerCase() + "%");
                    predicates.add(accNamePredicate);
                }

                if (status != null) {
                    Predicate statusPredicate = criteriaBuilder.equal(root.get(PaymentMethod_.STATUS), status);
                    predicates.add(statusPredicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
package net.woniper.board.repository.test;

import net.woniper.board.BoardApplication;
import net.woniper.board.domain.Bank;
import net.woniper.board.domain.BankBook;
import net.woniper.board.repository.CommonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by woniper on 15. 4. 2..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@WebAppConfiguration
@SpringApplicationConfiguration(classes = BoardApplication.class)
public class UserRepositoryTest {

    @Autowired private CommonRepository<Bank> bankRepository;
    @Autowired private CommonRepository<BankBook> bankBookRepository;

    @Test
    public void testName() throws Exception {

        Bank bank = new Bank();
        bank.setName("bank");
        BankBook bankBook = new BankBook();
        bankBook.setName("bankBook");

        bankRepository.save(bank);
        bankRepository.flush();
        bankBookRepository.save(bankBook);
        bankBookRepository.flush();

        System.out.println(bankRepository.findAll());
        System.out.println(bankBookRepository.findAll());

    }
}

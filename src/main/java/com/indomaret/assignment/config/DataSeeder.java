package com.indomaret.assignment.config;

import com.indomaret.assignment.entity.*;
import com.indomaret.assignment.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProvinceRepository provinceRepository;
    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;
    private final WhitelistedStoreRepository whitelistedStoreRepository;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      ProvinceRepository provinceRepository, BranchRepository branchRepository,
                      StoreRepository storeRepository, WhitelistedStoreRepository whitelistedStoreRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.provinceRepository = provinceRepository;
        this.branchRepository = branchRepository;
        this.storeRepository = storeRepository;
        this.whitelistedStoreRepository = whitelistedStoreRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Seed Admin User
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("password"))
                    .role("ROLE_ADMIN")
                    .isActive(true)
                    .isDeleted(false)
                    .build();
            userRepository.save(admin);
            System.out.println("====== SYSTEM ADMIN USER PROVISIONED ======");
        }

        // 2. Seed Master Data (Provinces, Branches, Stores)
        if (provinceRepository.count() == 0) {
            
            // Create Provinces
            Province dkiJakarta = Province.builder().code("PROV-JKT").name("DKI Jakarta").build();
            Province jawaBarat = Province.builder().code("PROV-JBR").name("Jawa Barat").build();
            provinceRepository.save(dkiJakarta);
            provinceRepository.save(jawaBarat);

            // Create Branches
            Branch jakartaBranch = Branch.builder()
                    .province(dkiJakarta)
                    .code("BR-JKT-01")
                    .name("Cabang Jakarta Pusat")
                    .address("Jl. MH Thamrin, Jakarta")
                    .build();
            
            Branch bekasiBranch = Branch.builder()
                    .province(jawaBarat)
                    .code("BR-BKS-01")
                    .name("Cabang Bekasi Barat")
                    .address("Jl. Ahmad Yani No. 15, Bekasi, Jawa Barat")
                    .build();
            
            branchRepository.save(jakartaBranch);
            branchRepository.save(bekasiBranch);

            // Create Stores
            Store flagshipStore = Store.builder()
                    .branch(jakartaBranch)
                    .code("STR-JKT-001")
                    .name("Indomaret Sudirman Flagship")
                    .address("Jl. Jend. Sudirman Kav. 21, DKI Jakarta")
                    .build();

            Store dagoStore = Store.builder()
                    .branch(bekasiBranch) 
                    .code("STR-JBR-019")
                    .name("Indomaret Dago Atas")
                    .address("Jl. Ir. H. Juanda No. 88, Bandung, Jawa Barat")
                    .build();

            storeRepository.save(flagshipStore);
            storeRepository.save(dagoStore);

            // 3. Seed Whitelist Store
            WhitelistedStore whitelistEntry = WhitelistedStore.builder()
                    .store(flagshipStore)
                    .reason("24-Hour National Flagship Store")
                    .build();
            whitelistedStoreRepository.save(whitelistEntry);

            System.out.println("====== MASTER DATA SEEDED SUCCESSFULLY ======");
        }
    }
}
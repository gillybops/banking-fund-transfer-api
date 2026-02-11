# ✅ QUICK START CHECKLIST

Complete these steps in order. Total time: ~60-90 minutes from code to live demo.

---

## Phase 1: Setup (10 min)

- [ ] Download/extract the BankingAPI folder
- [ ] Open in GitHub Codespaces or your IDE
- [ ] Verify Java 21+ installed: `java -version`
- [ ] Verify Maven installed: `mvn -version`

---

## Phase 2: Build & Test Locally (15 min)

- [ ] Navigate to project: `cd BankingAPI`
- [ ] Build project: `mvn clean install`
- [ ] Verify tests pass (look for "BUILD SUCCESS")
- [ ] Start app: `./start.sh` or `mvn spring-boot:run`
- [ ] Test locally:
  - [ ] Open Swagger UI: http://localhost:8080/swagger-ui.html
  - [ ] Create a test account via Swagger
  - [ ] Get account balance
- [ ] Stop app (Ctrl+C)

---

## Phase 3: GitHub Repository (5 min)

- [ ] Create GitHub account (if needed)
- [ ] Initialize git: `git init`
- [ ] Add files: `git add .`
- [ ] Commit: `git commit -m "Initial commit: Banking Fund Transfer API"`
- [ ] Create repo on GitHub (public)
- [ ] Push code:
  ```bash
  git remote add origin https://github.com/YOUR_USERNAME/banking-fund-transfer-api.git
  git branch -M main
  git push -u origin main
  ```

---

## Phase 4: Deploy to Render.com (20 min)

- [ ] Sign up at https://render.com (free)
- [ ] Click "New +" → "Web Service"
- [ ] Connect your GitHub repository
- [ ] Configure service:
  - Name: `banking-api`
  - Environment: `Java`
  - Build Command: `mvn clean install`
  - Start Command: `java -jar target/banking-api-1.0.0.jar`
  - Instance Type: `Free`
- [ ] Click "Create Web Service"
- [ ] Wait for deployment (~10 min)
- [ ] Save your URL: `https://banking-api-XXXXX.onrender.com`
- [ ] Test deployed API (first request may take 30 sec to wake up)

---

## Phase 5: Prepare Demo Data (10 min)

Replace `YOUR_API_URL` with your Render URL in these commands:

- [ ] **Create Alice's account:**
  ```bash
  curl -X POST YOUR_API_URL/api/v1/accounts \
    -u admin:admin \
    -H "Content-Type: application/json" \
    -d '{"accountHolderName":"Alice Smith","initialBalance":5000.00,"currency":"USD"}'
  ```
  **Save Alice's account number:** `________________`

- [ ] **Create Bob's account:**
  ```bash
  curl -X POST YOUR_API_URL/api/v1/accounts \
    -u admin:admin \
    -H "Content-Type: application/json" \
    -d '{"accountHolderName":"Bob Johnson","initialBalance":3000.00,"currency":"USD"}'
  ```
  **Save Bob's account number:** `________________`

- [ ] **Test a transfer:**
  ```bash
  curl -X POST YOUR_API_URL/api/v1/transfers \
    -u admin:admin \
    -H "Content-Type: application/json" \
    -d '{"fromAccountNumber":"ALICE_ACCT","toAccountNumber":"BOB_ACCT","amount":500.00,"description":"Test transfer"}'
  ```

- [ ] **Verify balances changed:**
  ```bash
  curl YOUR_API_URL/api/v1/accounts/ALICE_ACCT/balance -u admin:admin
  curl YOUR_API_URL/api/v1/accounts/BOB_ACCT/balance -u admin:admin
  ```

---

## Phase 6: Prepare for Presentation (10 min)

- [ ] Test Swagger UI on deployed URL
- [ ] Bookmark these URLs in browser:
  - [ ] `YOUR_API_URL/swagger-ui.html`
  - [ ] `https://github.com/YOUR_USERNAME/banking-fund-transfer-api`
- [ ] Practice demo once:
  1. Show Swagger UI
  2. Create account
  3. Execute transfer
  4. Show error (insufficient funds)
- [ ] Read PRESENTATION.md
- [ ] Prepare to discuss:
  - [ ] ACID transactions
  - [ ] Concurrency handling
  - [ ] Error handling
  - [ ] Testing strategy

---

## Optional Enhancements (if time permits)

- [ ] Add custom domain
- [ ] Import Postman collection
- [ ] Record demo video as backup
- [ ] Add more test accounts
- [ ] Practice explaining TransferService.java code

---

## Pre-Interview Checklist (Day Before)

- [ ] Verify API is still running (free tier may sleep)
- [ ] Wake up API by making a request
- [ ] Test all demo scenarios one more time
- [ ] Have GitHub repo open in one tab
- [ ] Have Swagger UI open in another tab
- [ ] Have PRESENTATION.md open for reference
- [ ] Charge laptop/check internet connection
- [ ] Prepare backup plan (local demo if needed)

---

## During Interview

- [ ] Stay calm and confident
- [ ] Start with 30-second intro
- [ ] Show live demo first (2 min)
- [ ] Walk through code (2 min)
- [ ] Answer questions (remaining time)
- [ ] Mention rapid learning ability
- [ ] Express enthusiasm for the role

---

## Success Metrics

You're ready when:
- ✅ API deployed and accessible
- ✅ Swagger UI loads correctly
- ✅ Can create accounts successfully
- ✅ Can execute transfers successfully
- ✅ Errors handled gracefully
- ✅ Can explain ACID properties
- ✅ Can explain the code structure

---

## Emergency Contacts

If something breaks:
1. Check Render deployment logs
2. Test locally in Codespaces
3. Have screenshots as backup
4. Focus on explaining concepts over demo

---

## Key URLs to Memorize

**Production:**
- API: `https://your-api-url`
- Swagger: `https://your-api-url/swagger-ui.html`

**Development:**
- Local API: `http://localhost:8080`
- Local Swagger: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

**Documentation:**
- GitHub: `https://github.com/YOUR_USERNAME/banking-fund-transfer-api`
- README: See in repo
- API Docs: `https://your-api-url/api-docs`

---

## Credentials

**API Authentication:**
- Admin: `admin` / `admin`
- User: `user` / `password`

**H2 Console (local only):**
- URL: `jdbc:h2:mem:bankingdb`
- Username: `sa`
- Password: (leave blank)

---

**Good luck! You've got this! 🚀**

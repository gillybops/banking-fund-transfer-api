# 🚀 Complete Deployment Guide

Follow these steps in order to go from code to live demo on your domain.

## ⏱️ Timeline Overview
- **Step 1-2**: Setup (10 min)
- **Step 3-4**: Build & Test (15 min)
- **Step 5-6**: Deploy (20 min)
- **Step 7**: Demo Prep (15 min)
- **Total**: ~60-70 minutes to live deployment

---

## Step 1: Setup GitHub Repository (5 min)

### In GitHub Codespaces Terminal:

```bash
# Navigate to the project directory
cd /workspace

# If not already in BankingAPI folder, the code is ready
# Initialize git repository
cd BankingAPI
git init

# Configure git (use your details)
git config --global user.email "your.email@example.com"
git config --global user.name "Your Name"

# Add all files
git add .
git commit -m "Initial commit: Banking Fund Transfer API"
```

### Create GitHub Repository:

**Option A - Using GitHub CLI:**
```bash
gh auth login
gh repo create banking-fund-transfer-api --public --source=. --remote=origin --push
```

**Option B - Via GitHub Web:**
1. Go to https://github.com/new
2. Name: `banking-fund-transfer-api`
3. Public repository
4. Don't initialize with README (we already have one)
5. Click "Create repository"
6. Follow the commands to push existing repository:
```bash
git remote add origin https://github.com/YOUR_USERNAME/banking-fund-transfer-api.git
git branch -M main
git push -u origin main
```

---

## Step 2: Verify Project Structure (2 min)

```bash
# Check all files are present
ls -la

# You should see:
# - src/ (source code)
# - pom.xml (Maven config)
# - README.md
# - Dockerfile
# - docker-compose.yml
```

---

## Step 3: Build the Project (5 min)

```bash
# Clean and build
mvn clean install

# This will:
# 1. Download all dependencies
# 2. Compile the code
# 3. Run all tests
# 4. Create JAR file in target/
```

**Expected output**: `BUILD SUCCESS`

---

## Step 4: Test Locally (5 min)

### Start the Application:
```bash
mvn spring-boot:run
```

Wait for:
```
Started BankingApiApplication in X.XXX seconds
```

### Test in Another Terminal:

```bash
# Create an account
curl -X POST http://localhost:8080/api/v1/accounts \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "Test User",
    "initialBalance": 1000.00,
    "currency": "USD"
  }'

# You should get back account details with account number
```

**Open Swagger UI**: 
- Forward port 8080 in Codespaces
- Visit: `https://YOUR-CODESPACE-URL/swagger-ui.html`

Stop the application (Ctrl+C) once verified.

---

## Step 5: Deploy to Render.com (15 min)

Render.com offers **free tier** with automatic HTTPS!

### 5.1: Create Render Account
1. Go to https://render.com
2. Sign up with GitHub (easier integration)

### 5.2: Create Web Service
1. Click "New +" → "Web Service"
2. Connect your GitHub repository: `banking-fund-transfer-api`
3. Configure:
   - **Name**: `banking-api`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean install`
   - **Start Command**: `java -jar target/banking-api-1.0.0.jar`
   - **Instance Type**: Free

### 5.3: Advanced Settings (Optional)
Environment Variables:
```
JAVA_TOOL_OPTIONS=-Xmx512m
```

### 5.4: Deploy
1. Click "Create Web Service"
2. Wait 5-10 minutes for first deployment
3. Monitor logs for: `Started BankingApiApplication`

### 5.5: Get Your URL
Your API will be at: `https://banking-api-xxxxx.onrender.com`

**Note**: Free tier sleeps after 15 min of inactivity. First request may take 30 seconds to wake up.

---

## Step 6: Configure Custom Domain (Optional, 10 min)

### If you have a domain (e.g., from Namecheap, GoDaddy):

1. **In Render Dashboard**:
   - Go to your service → Settings → Custom Domain
   - Click "Add Custom Domain"
   - Enter: `api.yourdomain.com`

2. **In Your Domain Provider**:
   - Add CNAME record:
     - **Name**: `api`
     - **Value**: `banking-api-xxxxx.onrender.com`
     - **TTL**: 3600

3. **Wait for DNS** (5-60 minutes)

4. **Render Auto-SSL**: Free HTTPS certificate is automatically provisioned

Your API will be at: `https://api.yourdomain.com`

---

## Step 7: Prepare Demo Data (5 min)

### Create Sample Accounts:

```bash
# Replace YOUR_DOMAIN with your actual URL
API_URL="https://banking-api-xxxxx.onrender.com"

# Account 1: Alice
curl -X POST $API_URL/api/v1/accounts \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "Alice Smith",
    "initialBalance": 5000.00,
    "currency": "USD"
  }'
# Save the account number from response!

# Account 2: Bob
curl -X POST $API_URL/api/v1/accounts \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "Bob Johnson",
    "initialBalance": 3000.00,
    "currency": "USD"
  }'
# Save this account number too!
```

### Test a Transfer:

```bash
# Replace with actual account numbers
curl -X POST $API_URL/api/v1/transfers \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountNumber": "XXXX-XXXX-XXXX",
    "toAccountNumber": "YYYY-YYYY-YYYY",
    "amount": 500.00,
    "description": "Demo transfer"
  }'
```

---

## Step 8: Present to Recruiter

### What to Show:

1. **GitHub Repository**
   - Clean code structure
   - Comprehensive README
   - Tests included
   - Show a specific class (e.g., `TransferService.java`) to explain ACID

2. **Live Swagger UI**
   - `https://your-api-url/swagger-ui.html`
   - Interactive API documentation
   - Try executing a transfer live

3. **Demonstrate Key Features**:

   **A. Successful Transfer**
   ```bash
   # Show the transfer request and response
   # Verify balances changed
   ```

   **B. Error Handling**
   ```bash
   # Try transferring more than available balance
   # Show clean error message
   ```

   **C. Concurrent Safety**
   - Explain the pessimistic locking
   - Show the `@Transactional` annotation
   - Discuss ACID guarantees

4. **Code Walkthrough** (5 min max):
   - Show `TransferService.executeTransfer()`
   - Explain transaction isolation
   - Point out validation and error handling

### Talking Points:

✅ "I built this in under 2 hours to demonstrate I can rapidly learn Java"

✅ "Key concepts I implemented:
   - ACID transactions for data integrity
   - Pessimistic locking for concurrent safety
   - RESTful API design
   - Comprehensive testing
   - Production-ready error handling"

✅ "While I'm experienced in Python, this shows I can quickly adapt to new languages and enterprise frameworks"

✅ "The API is fully deployed, tested, and documented - ready for integration"

---

## Alternative Deployment Options

### Railway.app (Also Free Tier):

```bash
npm i -g @railway/cli
railway login
railway init
railway up
```

Railway automatically detects Java and builds with Maven.

### Heroku (Free tier discontinued, but still an option):

```bash
# Create Procfile
echo "web: java -jar target/banking-api-1.0.0.jar" > Procfile

# Deploy
heroku login
heroku create banking-api-demo
git push heroku main
```

---

## Troubleshooting

### Build Fails:
```bash
# Clear Maven cache
rm -rf ~/.m2/repository
mvn clean install
```

### Port Already in Use:
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Render Deployment Fails:
- Check logs in Render dashboard
- Ensure Java version is 17+
- Verify pom.xml artifact name matches start command

### Can't Access Deployed API:
- Wait for deployment to complete (check logs)
- Free tier: First request may take 30 sec to wake up
- Check CORS if accessing from browser

---

## Next Steps (If Time Permits)

1. **Add JWT Authentication**
2. **Implement PostgreSQL** (instead of H2)
3. **Add Transaction History** endpoint
4. **Create Frontend UI** (React/Vue)
5. **Add Prometheus Metrics**
6. **Implement Rate Limiting**

---

**You're now ready to impress JP Morgan! 🎯**

The complete project demonstrates:
- ✅ Rapid Java learning
- ✅ Enterprise patterns
- ✅ Production deployment
- ✅ Professional documentation
- ✅ Testing best practices
- ✅ Real-world problem solving

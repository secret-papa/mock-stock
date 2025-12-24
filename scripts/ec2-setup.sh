#!/bin/bash

# EC2 초기 설정 스크립트 (Amazon Linux 2023 / Ubuntu)
# 이 스크립트를 EC2에서 직접 실행하세요

set -e

echo "=== Mock Stock EC2 Setup ==="

# 패키지 업데이트
echo "Updating packages..."
if command -v yum &> /dev/null; then
    sudo yum update -y
    sudo yum install -y java-21-amazon-corretto-headless
elif command -v apt-get &> /dev/null; then
    sudo apt-get update -y
    sudo apt-get install -y openjdk-21-jre-headless
fi

# 앱 디렉토리 생성
echo "Creating app directory..."
mkdir -p ~/app
cd ~/app

# 환경변수 파일 생성 (수동으로 값 입력 필요)
if [ ! -f .env ]; then
    echo "Creating .env template..."
    cat > .env << 'EOF'
DB_URL=jdbc:mysql://YOUR_RDS_ENDPOINT:3306/mock_stock
DB_USERNAME=YOUR_DB_USERNAME
DB_PASSWORD=YOUR_DB_PASSWORD
EOF
    echo "Please edit ~/app/.env with your database credentials"
fi

# systemd 서비스 파일 생성 (선택사항)
echo "Creating systemd service..."
sudo tee /etc/systemd/system/mock-stock.service > /dev/null << EOF
[Unit]
Description=Mock Stock Application
After=network.target

[Service]
Type=simple
User=$USER
WorkingDirectory=/home/$USER/app
EnvironmentFile=/home/$USER/app/.env
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod -Dserver.port=8080 app.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable mock-stock

echo ""
echo "=== Setup Complete ==="
echo ""
echo "Next steps:"
echo "1. Edit ~/app/.env with your database credentials"
echo "2. Configure GitHub Secrets:"
echo "   - EC2_HOST: Your EC2 public IP or domain"
echo "   - EC2_USERNAME: $USER"
echo "   - EC2_SSH_KEY: Your private SSH key"
echo ""
echo "3. Push to main branch to trigger deployment"
echo ""
echo "Manual commands:"
echo "  Start:   sudo systemctl start mock-stock"
echo "  Stop:    sudo systemctl stop mock-stock"
echo "  Status:  sudo systemctl status mock-stock"
echo "  Logs:    tail -f ~/app/app.log"

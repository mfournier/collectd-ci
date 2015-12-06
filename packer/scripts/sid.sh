apt-get -y install sudo rsync

echo 'deb http://mirrors.digitalocean.com/debian sid main contrib non-free' > /etc/apt/sources.list
echo 'APT::Install-Recommends "0";' > /etc/apt/apt.conf.d/50no-install-recommends
echo 'APT::Install-Suggests "0";' > /etc/apt/apt.conf.d/50no-install-suggests

echo "net.ipv6.conf.all.disable_ipv6 = 1" >> /etc/sysctl.conf
sysctl -p

apt-get -y update
apt-get -y upgrade
apt-get -y dist-upgrade

apt-get -y install autoconf automake bison clang cpp dpkg-dev flex gcc gdb git libc6-dev libtool m4 make musl-dev musl-tools pkg-config strace valgrind
apt-get -y install autotools-dev default-jdk iptables-dev javahelper libatasmart-dev libcap-dev libcurl4-gnutls-dev libdbi-dev libesmtp-dev libganglia1-dev libgcrypt20-dev libglib2.0-dev libgps-dev libhiredis-dev libi2c-dev libldap2-dev libltdl-dev liblvm2-dev libmemcached-dev libmnl-dev libmodbus-dev libmosquitto-dev libmysqlclient-dev libnotify-dev libopenipmi-dev liboping-dev libow-dev libpcap-dev libperl-dev libpq-dev libprotobuf-c0-dev librabbitmq-dev librdkafka-dev librrd-dev libsensors4-dev libsigrok-dev libsnmp-dev libstatgrab-dev libtokyocabinet-dev libtokyotyrant-dev libudev-dev libupsclient-dev libvarnish-dev libvirt-dev libxml2-dev libyajl-dev linux-libc-dev perl protobuf-c-compiler python-dev

apt-get -y clean

mkdir -p /opt/jenkins
ln -s /usr/lib/jvm/java-7-openjdk-$(dpkg --print-architecture)/bin/java /opt/jenkins/
